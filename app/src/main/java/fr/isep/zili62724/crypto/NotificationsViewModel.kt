package fr.isep.zili62724.crypto

import fr.isep.zili62724.crypto.Event
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NotificationsViewModel : ViewModel() {

    private val _showNotificationEvent = MutableLiveData<Event<Unit>>()
    val showNotificationEvent get() = _showNotificationEvent

    // Function to simulate an event that triggers the notification
    fun simulateNotificationEvent() {
        viewModelScope.launch {
            // Simulate some background work
            delay(2000)

            // Trigger the notification event
            _showNotificationEvent.value = Event(Unit)
        }
    }
}