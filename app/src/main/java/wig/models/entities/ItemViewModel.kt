package wig.models.entities

import androidx.lifecycle.ViewModel

class ItemViewModel : ViewModel() {
    var name: String = ""
    var tags: String = ""
    var description: String = ""
    var qr: String = ""
}