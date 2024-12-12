package com.example.mindspace.questionnaire

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class QuestionnaireViewModel : ViewModel() {
    private val _studySatisfaction = MutableLiveData<Int>()
    private val _academicPressure = MutableLiveData<Int>()
    private val _financialConcerns = MutableLiveData<Int>()
    private val _socialRelationships = MutableLiveData<Int>()
    private val _depression = MutableLiveData<Int>()
    private val _anxiety = MutableLiveData<Int>()
    private val _isolation = MutableLiveData<Int>()
    private val _futureInsecurity = MutableLiveData<Int>()
    private val _cgpa = MutableLiveData<String>()
    private val _averageSleep = MutableLiveData<String>()

    val studySatisfaction: LiveData<Int> get() = _studySatisfaction
    val academicPressure: LiveData<Int> get() = _academicPressure
    val financialConcerns: LiveData<Int> get() = _financialConcerns
    val socialRelationships: LiveData<Int> get() = _socialRelationships
    val depression: LiveData<Int> get() = _depression
    val anxiety: LiveData<Int> get() = _anxiety
    val isolation: LiveData<Int> get() = _isolation
    val futureInsecurity: LiveData<Int> get() = _futureInsecurity
    val cgpa: LiveData<String> get() = _cgpa
    val averageSleep: LiveData<String> get() = _averageSleep

    fun setStudySatisfaction(value: Int) { _studySatisfaction.value = value }
    fun setAcademicPressure(value: Int) { _academicPressure.value = value }
    fun setFinancialConcerns(value: Int) { _financialConcerns.value = value }
    fun setSocialRelationships(value: Int) { _socialRelationships.value = value }
    fun setDepression(value: Int) { _depression.value = value }
    fun setAnxiety(value: Int) { _anxiety.value = value }
    fun setIsolation(value: Int) { _isolation.value = value }
    fun setFutureInsecurity(value: Int) { _futureInsecurity.value = value }
    fun setCgpa(value: String) { _cgpa.value = value }
    fun setAverageSleep(value: String) { _averageSleep.value = value }
}
