/*
 * Copyright (c) 2019 Muhammad Utsman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.utsman.kemana.remote.driver

import com.utsman.kemana.base.logi
import com.utsman.kemana.remote.printThrow
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class RemotePresenter(private val composite: CompositeDisposable) : RemoteListener {
    private val remoteInstance = RemoteInstance.create()

    override fun insertDriver(
        driverItem: Driver,
        driver: (success: Boolean, driver: Driver?) -> Unit
    ) {
        val action = remoteInstance.insertDriver(driverItem)
            .subscribeOn(Schedulers.io())
            .map { it.data }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (!it.isNullOrEmpty()) {
                    driver.invoke(true, it[0])
                    logi("driver is ${it[0]}")
                }
            }, {
                it.printThrow("insert driver")
                driver.invoke(false, null)
            })

        composite.add(action)
    }

    override fun getDriversActive(list: (List<Driver>?) -> Unit) {
        val action = remoteInstance.getAllDriver()
            .subscribeOn(Schedulers.io())
            .map { it.data }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                logi("driver list success")
                list.invoke(it)
            }, {
                it.printThrow("driver list")
            })

        composite.add(action)
    }

    override fun getDriversActiveEmail(email: (List<String>?) -> Unit) {
        val action = remoteInstance.getAllDriverEmail()
            .subscribeOn(Schedulers.io())
            .map { it.data }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                logi("driver list success")
                email.invoke(it)
            }, {
                it.printThrow("driver list")
            })

        composite.add(action)
    }

    override fun getDriver(id: String, driver: (Driver?) -> Unit) {
        val action = remoteInstance.getDriver(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val list = it.data
                if (!list.isNullOrEmpty()) {
                    driver.invoke(list[0])
                } else {
                    driver.invoke(null)
                }

            }, {
                it.printThrow("get driver")
            })

        composite.add(action)
    }

    override fun getDriver(id: String): Driver? {
        var driver: Driver? = null

        val action = remoteInstance.getDriver(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

                val list = it.data
                driver = if (!list.isNullOrEmpty()) {
                    list[0]
                } else {
                    null
                }

            }, {
                it.printThrow("get driver")
            })

        composite.add(action)

        return driver
    }

    override fun editDriver(id: String, position: Position, driver: (Driver?) -> Unit) {
        val action = remoteInstance.editDriver(id, position)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val list = it.data
                if (!list.isNullOrEmpty()) {
                    driver.invoke(list[0])
                } else {
                    driver.invoke(null)
                }

            }, {
                it.printThrow("get driver edit")
            })

        composite.add(action)
    }

    override fun editDriverByEmail(email: String, position: Position, driver: (Driver?) -> Unit) {
        val action = remoteInstance.editDriverByEmail(email, position)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val list = it.data
                if (!list.isNullOrEmpty()) {
                    driver.invoke(list[0])
                } else {
                    driver.invoke(null)
                }

            }, {
                it.printThrow("get driver edit")
            })

        composite.add(action)
    }

    override fun deleteDriver(id: String, status: (Boolean?) -> Unit) {
        val action = remoteInstance.deleteDriver(id)
            .subscribeOn(Schedulers.io())
            .map { it.message }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it == "OK") status.invoke(true)
                else status.invoke(null)
            }, {
                status.invoke(false)
                it.printThrow("delete fail")
            })

        composite.add(action)
    }

    override fun deleteDriverByEmail(email: String, status: (Boolean?) -> Unit) {
        val action = remoteInstance.deleteDriverByEmail(email)
            .subscribeOn(Schedulers.io())
            .map { it.message }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it == "OK") status.invoke(true)
                else status.invoke(null)
            }, {
                status.invoke(false)
                it.printThrow("delete fail")
            })

        composite.add(action)
    }

    override fun getDriversRegisteredEmail(email: String?, driver: (Driver?) -> Unit) {
        val action = remoteInstance.getRegisteredDriverByEmail(email)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val list = it.data
                if (!list.isNullOrEmpty()) {
                    driver.invoke(list[0])
                } else {
                    driver.invoke(null)
                }

            }, {
                it.printThrow("get driver")
            })

        composite.add(action)
    }

    override fun registerDriver(
        driverItem: Driver,
        driver: (success: Boolean, driver: Driver?) -> Unit
    ) {
        val action = remoteInstance.registerDriver(driverItem)
            .subscribeOn(Schedulers.io())
            .map { it.data }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (!it.isNullOrEmpty()) {
                    driver.invoke(true, it[0])
                    logi("driver is ${it[0]}")
                }
            }, {
                it.printThrow("insert driver")
                driver.invoke(false, null)
            })

        composite.add(action)
    }

    override fun checkRegisteredDriver(email: String?, hasRegister: (Boolean?) -> Unit) {
        val action = remoteInstance.checkRegisteredDriver(email)
            .subscribeOn(Schedulers.io())
            .map { it.data }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                hasRegister.invoke(it)
                logi("status is --> $it")
            }, {
                it.printThrow("insert driver")
                hasRegister.invoke(null)
            })

        composite.add(action)
    }

    override fun getRegisteredDriverById(id: String?, driver: (Driver?) -> Unit) {
        val action = remoteInstance.getRegisteredDriver(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val list = it.data
                if (!list.isNullOrEmpty()) {
                    driver.invoke(list[0])
                } else {
                    driver.invoke(null)
                }

            }, {
                it.printThrow("get driver")
            })

        composite.add(action)
    }

    override fun getAttrRegisteredDriver(id: String?, attr: (Attribute?) -> Unit) {
        val action = remoteInstance.getAttrRegisteredDriver(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val list = it.attrs
                if (!list.isNullOrEmpty()) {
                    attr.invoke(list[0])
                } else {
                    attr.invoke(null)
                }

            }, {
                it.printThrow("get attr driver")
            })

        composite.add(action)
    }

    override fun editDriverRegisteredByEmail(
        email: String?,
        position: Position?,
        driver: (Driver?) -> Unit
    ) {
        val action = remoteInstance.editDriverRegisteredByEmail(email, position)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val list = it.data
                if (!list.isNullOrEmpty()) {
                    driver.invoke(list[0])
                } else {
                    driver.invoke(null)
                }

            }, {
                it.printThrow("get driver edit")
            })

        composite.add(action)
    }

}