package net.horizonsend.ion.proxy.database

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
	private val client = createClient("mongodb://${ionConfig.username}:${ionConfig.password}@${ionConfig.host}:${ionConfig.port}/${ionConfig.database}")
	private val database = client.getDatabase(ionConfig.database)

	private val accountDataCollection = database.getCollection<AccountData>()

	init {
		// https://github.com/Litote/kmongo/issues/98
		System.setProperty("org.litote.mongo.test.mapping.service", "org.litote.kmongo.jackson.JacksonClassMappingTypeService")
	}

	@Subscribe
	fun onProxyShutdown(event: ProxyShutdownEvent) = client.close()

	fun getAccountData(uuid: UUID): AccountData? = accountDataCollection.findOneById(uuid)
	fun saveAccountData(accountData: AccountData) = accountDataCollection.replaceOneById(accountData._id, accountData)
}