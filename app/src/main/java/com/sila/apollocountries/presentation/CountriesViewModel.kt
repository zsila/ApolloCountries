package com.sila.apollocountries.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sila.apollocountries.domain.CountryClient
import com.sila.apollocountries.domain.DetailedCountry
import com.sila.apollocountries.domain.SimpleCountry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountriesViewModel @Inject constructor(
    private val countryClient: CountryClient
): ViewModel() {


    private val _state = MutableStateFlow(CountriesState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.update { it.copy(
                isLoading = true
            ) }

            _state.update { it.copy(
                countries = getCountries(countryClient),
                isLoading = false
            )
            }
        }
    }

    suspend fun getCountries(countryClient: CountryClient): List<SimpleCountry> {
        return countryClient
            .getCountries()
            .sortedBy { it.name }
    }

     fun selectCountry(code: String) {
        viewModelScope.launch {
            _state.update {it.copy(
                selectedCountry = countryClient.getCountry(code)
            )
            }
        }
    }

    fun dismissCountryDialog(){
        _state.update {it.copy(
            selectedCountry = null
        )
        }
    }

    data class CountriesState(
        val countries: List<SimpleCountry> = emptyList(),
        val isLoading: Boolean = false,
        val selectedCountry: DetailedCountry? = null
    )
}