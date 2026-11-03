package hu.bsstudio.bssweb.revision.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.envers.RevisionNumber
import org.hibernate.envers.RevisionTimestamp
import java.time.Instant
import org.hibernate.envers.RevisionEntity as EnversRevisionEntity

/** One revision: a transaction that changed audited entities, when it happened and who made it. */
@Entity
@Table(name = "revinfo")
@EnversRevisionEntity(UsernameRevisionListener::class)
class RevisionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @RevisionNumber
    @Column(name = "rev")
    var id: Long = 0

    @RevisionTimestamp
    lateinit var timestamp: Instant

    /** The name of the signed-in user, or `null` when nobody was signed in. */
    var username: String? = null
}
