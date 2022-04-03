package com.dictionary.data.retrofit.dto

import com.dictionary.domain.entity.Translation
import com.dictionary.domain.entity.TranslationWord

data class TranslationDto(
    val def: List<Definition>,
){
    fun toEntity(): Translation {
        var translationWords: MutableList<TranslationWord> = mutableListOf()
        for (definition in def) {
            val translations: MutableList<String> = mutableListOf()

            if (definition.tr == null) {
                continue
            }

            for (translation in definition.tr) {
                translations.add(translation.text)

                if (translation.syn == null) {
                    continue
                }

                for (synonym in translation.syn) {
                    translations.add(synonym.text)
                }
            }

            translationWords.add(
                TranslationWord(
                    word = definition.text,
                    type = definition.pos,
                    translation = translations
                )
            )
        }

        return Translation(translationWords)
    }
}