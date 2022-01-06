package net.horizonsend.ion.proxy.database

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent
import java.util.UUID
import net.horizonsend.ion.proxy.Ion.Companion.ionConfig
import net.horizonsend.ion.proxy.database.data.AccountData
import org.litote.kmongo.KMongo.createClient
import org.litote.kmongo.findOneById
import org.litote.kmongo.getCollection
import org.litote.kmongo.replaceOneById

object MongoManager {
	private val client: MongoClient
	private val database: MongoDatabase

	private val accountDataCollection: MongoCollection<AccountData>

	init {
		// https://github.com/Litote/kmongo/issues/98
		System.setProperty("org.litote.mongo.test.mapping.service", "org.litote.kmongo.jackson.JacksonClassMappingTypeService")

		client = createClient("mongodb://${ionConfig.username}:${ionConfig.password}@${ionConfig.host}:${ionConfig.port}/${ionConfig.database}")

		database = client.getDatabase(ionConfig.database)

		accountDataCollection = database.getCollection()
	}

	@Subscribe
	@Suppress("UNUSED_PARAMETER") // Parameter is required to indicate what event to subscribe to
	fun onProxyShutdown(event: ProxyShutdownEvent) = client.close()

	fun getAccountData(uuid: UUID): AccountData? = accountDataCollection.findOneById(uuid)
	fun saveAccountData(accountData: AccountData) = accountDataCollection.replaceOneById(accountData._id, accountData)
}