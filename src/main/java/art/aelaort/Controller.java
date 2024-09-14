package art.aelaort;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class Controller {
	private final DataService dataService;

	@GetMapping("/random")
	public String random() {
		return dataService.getRandom();
	}

	@GetMapping("/")
	public String byRank(@RequestParam Integer rank) {
		return dataService.getByRank(rank);
	}
}
