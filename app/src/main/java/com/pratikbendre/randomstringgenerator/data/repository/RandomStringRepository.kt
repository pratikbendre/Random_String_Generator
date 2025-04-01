package com.pratikbendre.randomstringgenerator.data.repository

import com.pratikbendre.randomstringgenerator.data.remote.ContentProviderService
import com.pratikbendre.randomstringgenerator.model.RandomText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RandomStringRepository @Inject constructor(private val contentProviderService: ContentProviderService) {

    fun getString(text_length: Int): Flow<RandomText> {
        return flow { contentProviderService.generateString(text_length)?.let { emit(it) } }
    }

}


