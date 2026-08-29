package org.molkkytracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Un joueur mémorisé par l'utilisateur pour un remplissage rapide
 * lors des prochaines parties (retrouvé dans le volet déroulant du
 * champ "prénom/pseudo").
 */
@Entity(tableName = "remembered_players")
data class Player(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String
)
