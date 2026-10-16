package hu.bsstudio.bssweb.dashboard.page

import hu.bsstudio.bssweb.metrics.service.MetricsService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/admin")
class DashboardPageController(
    private val metricsService: MetricsService,
) {
    @GetMapping
    fun dashboard(model: Model): String {
        model["metrics"] = metricsService.getMetrics()
        return "admin/dashboard"
    }
}
