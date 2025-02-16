package org.example

import io.exoquery.pprint

import com.turbopuffer.api.client.okhttp.TurbopufferOkHttpClient
import com.turbopuffer.api.client.TurbopufferClient
import com.turbopuffer.api.core.JsonValue
import com.turbopuffer.api.errors.TurbopufferServiceException
import com.turbopuffer.api.models.DocumentRow
import com.turbopuffer.api.models.NamespaceDeleteAllParams
import com.turbopuffer.api.models.NamespaceGetSchemaParams
import com.turbopuffer.api.models.NamespaceQueryParams
import com.turbopuffer.api.models.NamespaceUpsertParams
import com.turbopuffer.api.models.NamespaceUpsertParams.UpsertRowBased.DistanceMetric

fun main() {
    val client: TurbopufferClient = TurbopufferOkHttpClient.builder()
        .fromEnv()
        .build()

    val namespace = "nikhil-kotlin-test"
    println("Operating on namespace: $namespace")

    try {
        client.namespaces().deleteAll(
            NamespaceDeleteAllParams.builder()
                .namespace(namespace)
                .build()
        )
    } catch (e: TurbopufferServiceException) {
        println("Received error while deleting namespace: ${e.message} (code: ${e.statusCode()})")
        if (e.statusCode() == 404) {
            println("Namespace not found, continuing")
        } else {
            println("Error is fatal, exiting")
            System.exit(1)
        }
    }

    val upsert = client.namespaces().upsert(
        NamespaceUpsertParams.builder()
            .namespace(namespace)
            .forUpsertRowBased(NamespaceUpsertParams.UpsertRowBased.builder()
                .addUpsert(DocumentRow.builder()
                    .id("1")
                    .vector(listOf(1.0, 2.0, 3.0))
                    .attributes(DocumentRow.Attributes.builder()
                        .putAdditionalProperty("name", JsonValue.from("Nikhil"))
                        .putAdditionalProperty("age", JsonValue.from(30))
                        .build())
                    .build())
                .addUpsert(DocumentRow.builder()
                    .id("2")
                    .vector(listOf(4.0, 5.0, 6.0))
                    .attributes(DocumentRow.Attributes.builder()
                        .putAdditionalProperty("name", JsonValue.from("Katherine"))
                        .putAdditionalProperty("age", JsonValue.from(30))
                        .build())
                    .build())
                .distanceMetric(DistanceMetric.COSINE_DISTANCE)
                .build())
            .build()
    )
    println("Upsert status: ${upsert.status()}")

    var queryResults = client.namespaces().query(
        NamespaceQueryParams.builder()
            .namespace(namespace)
            .vector(listOf(3.0, 4.0, 5.0))
            .includeAttributes(true)
            .includeVectors(true)
            .topK(1)
            .build()
    )
    println("Query result:")
    println(pprint(queryResults, defaultWidth = 80))

    val schema = client.namespaces().getSchema(
        NamespaceGetSchemaParams.builder()
            .namespace(namespace)
            .build()
    )
    println("Schema:")
    println(pprint(schema, defaultWidth = 80))
}
