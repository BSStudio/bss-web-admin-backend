package hu.bsstudio.bssweb

import hu.bsstudio.bssweb.event.controller.EventController
import io.restassured.module.mockmvc.RestAssuredMockMvc

class ContractTestBase {

    fun setup() {
        RestAssuredMockMvc.standaloneSetup()
    }
}
