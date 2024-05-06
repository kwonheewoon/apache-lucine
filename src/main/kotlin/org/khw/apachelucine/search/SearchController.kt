package org.khw.apachelucine.search

import lombok.RequiredArgsConstructor
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
class SearchController(
    val searchService: SearchService
) {

    @PostMapping
    fun indexing(@RequestParam("text")text: String){
        searchService.addToIndex(text)
    }

    @GetMapping
    fun search(@RequestParam("text")text: String): List<String>{
        return searchService.searchIndex(text)
    }
}