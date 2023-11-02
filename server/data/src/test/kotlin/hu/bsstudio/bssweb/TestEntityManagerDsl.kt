package hu.bsstudio.bssweb

import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager

inline fun <reified T> TestEntityManager.find(primaryKey: Any?): T {
    return this.find(T::class.java, primaryKey)
}

inline fun <reified T> TestEntityManager.persistAndGetId(entity: Any): T {
    return this.persistAndGetId(entity, T::class.java)
}

inline fun <reified T> TestEntityManager.getId(entity: Any): T {
    return this.getId(entity, T::class.java)
}
