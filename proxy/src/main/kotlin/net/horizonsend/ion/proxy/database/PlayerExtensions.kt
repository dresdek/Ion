package net.horizonsend.ion.proxy.database

import com.velocitypowered.api.proxy.Player
import net.horizonsend.ion.proxy.database.MongoManager.accountDataCollection
import net.horizonsend.ion.proxy.database.data.AccountData
import org.litote.kmongo.findOneById
import org.litote.kmongo.replaceOneById

var Player.accountData: AccountData
	get() {
		var accountData = accountDataCollection.findOneById(this.uniqueId.toString())

		if (accountData == null) {
			accountData = AccountData(this.uniqueId.toString()) // Create new account data
			accountDataCollection.insertOne(accountData) // Save the new account data
		}

		return accountData
	}
	set(value) { accountDataCollection.replaceOneById(this.uniqueId.toString(), value) }