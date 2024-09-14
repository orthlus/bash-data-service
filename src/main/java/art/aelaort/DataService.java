package art.aelaort;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.List;
import java.util.Random;
import java.util.RandomAccess;
import java.util.stream.Stream;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataService {
	private final ObjectMapper jacksonObjectMapper;
	@Value("${bash.file.url}")
	private URI fileUrl;
	private List<String> quotesArrayList;
	private final Random random = new Random();

	@PostConstruct
	private void init() throws Exception {
		Quote[] quotes = jacksonObjectMapper.readValue(fileUrl.toURL(), Quote[].class);

		quotesArrayList = Stream.of(quotes)
				.sorted((o1, o2) -> Integer.compare(o2.getRating(), o1.getRating()))
				.map(Quote::getText)
				.toList();

		if (!(quotesArrayList instanceof RandomAccess)) {
			throw new RuntimeException("wrong implementation list for quotes");
		}

		log.info("quotes loaded, size - {}", quotes.length);
	}

	public String getByRank(int rank) {
		if (rank < 1) {
			return "нужен положительный номер!";
		}
		try {
			return quotesArrayList.get(rank - 1);
		} catch (ArrayIndexOutOfBoundsException e) {
			return "столько нету";
		}
	}

	public String getRandom() {
		return getByRank(random.nextInt(1, quotesArrayList.size() + 1));
	}
}
