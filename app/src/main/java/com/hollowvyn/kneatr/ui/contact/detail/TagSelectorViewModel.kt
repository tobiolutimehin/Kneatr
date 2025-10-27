package com.hollowvyn.kneatr.ui.contact.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hollowvyn.kneatr.domain.model.ContactTag
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class TagSelectorViewModel : ViewModel() {
    private val _selectedTags = MutableStateFlow<Set<ContactTag>>(emptySet())
    val selectedTags: StateFlow<Set<ContactTag>> = _selectedTags.asStateFlow()

    private val _tagInput = MutableStateFlow("")
    val tagInput: StateFlow<String> = _tagInput.asStateFlow()

    private val allContactTags = MutableStateFlow<List<ContactTag>>(emptyList())

    private val _hasChanges = MutableStateFlow(false)
    val hasChanges: StateFlow<Boolean> = _hasChanges.asStateFlow()

    private var initialTags: Set<ContactTag> = emptySet()

    val suggestions: StateFlow<List<ContactTag>> =
        combine(
            _tagInput,
            allContactTags,
            _selectedTags,
        ) { input, allTags, selected ->
            getTagSuggestions(input, allTags, selected)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList(),
        )

    /**
     * Initialize the ViewModel with current tags and all available tags
     */
    fun initialize(
        currentTags: Set<ContactTag>,
        allTags: List<ContactTag>,
    ) {
        initialTags = currentTags
        _selectedTags.value = currentTags
        allContactTags.value = allTags
        _hasChanges.value = false
    }

    /**
     * Update the tag input field
     */
    fun onTagInputChanged(input: String) {
        _tagInput.value = normalizeTagInput(input)
    }

    /**
     * Add a tag to the selected tags
     */
    fun addTag(tagName: String) {
        val normalizedName = normalizeTagName(tagName)

        if (normalizedName.isBlank() || tagExists(normalizedName, _selectedTags.value)) {
            return
        }

        val tagToAdd =
            findTagByName(normalizedName, allContactTags.value)
                ?: ContactTag(name = normalizedName)

        _selectedTags.value = _selectedTags.value + tagToAdd
        _tagInput.value = ""
        updateHasChanges()
    }

    /**
     * Remove a tag from the selected tags
     */
    fun removeTag(tag: ContactTag) {
        _selectedTags.value = _selectedTags.value - tag
        updateHasChanges()
    }

    /**
     * Get the current selected tags to save
     */
    fun getSelectedTags(): Set<ContactTag> = _selectedTags.value

    private fun updateHasChanges() {
        _hasChanges.value = _selectedTags.value != initialTags
    }

    private fun normalizeTagInput(input: String): String =
        input
            .lowercase()
            .replace(Regex("[^a-z0-9_\\s]"), "")
            .replace(Regex("\\s+"), "_")
            .replace(Regex("_{2,}"), "_")

    private fun normalizeTagName(name: String): String = normalizeTagInput(name).trim('_')

    private fun findTagByName(
        name: String,
        allTags: List<ContactTag>,
    ): ContactTag? = allTags.find { it.name.equals(name, ignoreCase = true) }

    private fun tagExists(
        name: String,
        tags: Set<ContactTag>,
    ): Boolean = tags.any { it.name.equals(name, ignoreCase = true) }

    private fun getTagSuggestions(
        query: String,
        allTags: List<ContactTag>,
        excludeTags: Set<ContactTag>,
        limit: Int = 10,
    ): List<ContactTag> {
        val availableTags =
            allTags.filter { it.name.lowercase() !in excludeTags.map { it.name.lowercase() } }

        return if (query.isBlank()) {
            availableTags.shuffled().take(limit)
        } else {
            val normalizedQuery = normalizeTagInput(query)
            availableTags
                .filter { it.name.contains(normalizedQuery, ignoreCase = true) }
                .take(limit)
        }
    }
}
