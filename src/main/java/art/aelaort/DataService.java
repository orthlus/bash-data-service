package art.aelaort;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Comparator;
import java.util.List;
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

	@PostConstruct
	private void init() throws Exception {
		Quote[] quotes = jacksonObjectMapper.readValue(fileUrl.toURL(), Quote[].class);

		quotesArrayList = Stream.of(quotes)
				.sorted(Comparator.comparing(Quote::getRating))
				.map(Quote::getText)
				.toList();

		if (!(quotesArrayList instanceof RandomAccess)) {
			throw new RuntimeException("wrong implementation list for quotes");
		}

		log.info("quotes loaded, size - {}", quotes.length);
	}

	public String getByRank(int rank) {
		return quotesArrayList.get(rank);
	}
}
