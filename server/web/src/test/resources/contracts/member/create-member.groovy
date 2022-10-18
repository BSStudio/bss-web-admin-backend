package contracts.member

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method PUT()
        url '/api/member'
        body({
            url: 'url'
            name: 'name'
        })
    }
    response {
        status CREATED()
        body({

        })
    }
}
