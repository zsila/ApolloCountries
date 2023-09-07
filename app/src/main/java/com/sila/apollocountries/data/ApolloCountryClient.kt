package com.sila.apollocountries.data

import com.apollographql.apollo3.ApolloClient
import com.sila.CountriesQuery
import com.sila.CountryQuery
import com.sila.apollocountries.domain.CountryClient
import com.sila.apollocountries.domain.DetailedCountry
import com.sila.apollocountries.domain.SimpleCountry

class ApolloCountryClient(
    private val apolloClient: ApolloClient
): CountryClient {
    override suspend fun getCountries(): List<SimpleCountry> {
        return apolloClient
            .query(CountriesQuery())
            .execute()
            .data
            ?.countries
            ?.map { it.toSimpleCountry() }
            ?: emptyList()
    }

    override suspend fun getCountry(code: String): DetailedCountry? {
        return apolloClient
            .query(CountryQuery(code))
            .execute()
            .data
            ?.country
            ?.toDetailedCountry()
    }
}