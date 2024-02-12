package com.juul.datadog

// These functions only exist on the iOS and Android SDKs, so they have been separated out of the generic Logger interface.
public interface TagHandler {
    public fun addTag(tag: String)

    public fun removeTag(tag: String)

    public fun addTagWithKey(key: String, value: String)

    public fun removeTagsWithKey(key: String)
}
