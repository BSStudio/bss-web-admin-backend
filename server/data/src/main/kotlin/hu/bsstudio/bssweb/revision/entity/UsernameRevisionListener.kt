package hu.bsstudio.bssweb.revision.entity

import org.hibernate.envers.RevisionListener
import org.springframework.security.core.context.SecurityContextHolder

/** Stamps each new revision with the name of the user whose request made it. */
class UsernameRevisionListener : RevisionListener {
    override fun newRevision(revisionEntity: Any) {
        (revisionEntity as RevisionEntity).username = SecurityContextHolder.getContext().authentication?.name
    }
}
