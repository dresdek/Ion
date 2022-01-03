package net.horizonsend.ion.proxy.database

import com.velocitypowered.api.proxy.Player
import kotlin.random.Random
import net.horizonsend.ion.proxy.database.MongoManager.accountDataCollection
import net.horizonsend.ion.proxy.database.MongoManager.crossAccountDataCollection
import net.horizonsend.ion.proxy.database.data.AccountData
import net.horizonsend.ion.proxy.database.data.CrossAccountData
import org.litote.kmongo.findOneById
import org.litote.kmongo.replaceOneById

var Player.accountData: AccountData
	get() {
		var accountData = accountDataCollection.findOneById(this.uniqueId.toString())

		if (accountData == null) {
			// Randomly generate a new unique id for the cross account data
			var newId = Random.nextInt()
			while (crossAccountDataCollection.findOneById(newId) != null) { newId = Random.nextInt() }

			crossAccountDataCollection.insertOne(CrossAccountData(newId)) // Save the new cross account data

			accountData = AccountData(this.uniqueId.toString(), newId) // Create new account data

			accountDataCollection.insertOne(accountData) // Save the new account data
		}

		return accountData
	}
	set(value) { accountDataCollection.replaceOneById(this.uniqueId.toString(), value) }

var Player.crossAccountData: CrossAccountData
	get() = crossAccountDataCollection.findOneById(this.accountData.crossAccountDataId) ?: throw IllegalStateException("Account does not not have CrossAccountData!")
	set(value) { crossAccountDataCollection.replaceOneById(this.accountData.crossAccountDataId, value) }