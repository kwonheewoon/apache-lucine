package org.khw.apachelucine.search

import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.document.Document
import org.apache.lucene.document.Field
import org.apache.lucene.document.TextField
import org.apache.lucene.index.DirectoryReader
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.index.IndexWriterConfig
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.store.FSDirectory
import org.apache.lucene.util.QueryBuilder
import org.springframework.stereotype.Service
import java.nio.file.Paths

@Service
class SearchService {

    private val analyzer = StandardAnalyzer()
    // 인덱스 파일을 저장할 디렉토리 경로를 지정합니다. 경로는 시스템에 맞게 조정해야 합니다.
    private val indexPath = Paths.get(System.getProperty("user.dir"), "lucene_indexes")
    private val index = FSDirectory.open(indexPath)

    fun addToIndex(text: String) {
        val config = IndexWriterConfig(analyzer)
        IndexWriter(index, config).use { writer ->
            val doc = Document()
            doc.add(TextField("content", text, Field.Store.YES))
            writer.addDocument(doc)
        }
    }

    fun searchIndex(queryStr: String): List<String> {
        val query = QueryBuilder(analyzer).createBooleanQuery("content", queryStr)
        val reader = DirectoryReader.open(index)
        val searcher = IndexSearcher(reader)
        val hits = searcher.search(query, 10).scoreDocs

        return hits.map { hit ->
            val docId = hit.doc
            val doc = searcher.doc(docId)
            doc.get("content")
        }
    }
}