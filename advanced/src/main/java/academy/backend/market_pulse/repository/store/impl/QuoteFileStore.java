package academy.backend.market_pulse.repository.store.impl;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import academy.backend.market_pulse.factory.InstrumentFactories;
import academy.backend.market_pulse.model.Currency;
import academy.backend.market_pulse.model.Quote;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Заготовка для самостоятельной работы (см. «Требования к файловому хранилищу.md», семинар 7) —
 * структура есть, решения нет. Формат файла — на ваш выбор (JSON, CSV, построчный текст); главное,
 * чтобы {@code load(save(quotes))} возвращал те же котировки. Проверьте себя тестом
 * {@code QuoteFileStoreContractTest} (замените в нём эталонную реализацию на свою).
 */
public class QuoteFileStore implements academy.backend.market_pulse.repository.store.QuoteFileStore {
    private record QuoteRecord(String type, String ticker, String name,
                               Currency currency, BigDecimal price,
                               BigDecimal changePercent) {}
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void save(Path path, List<Quote> quotes) {
        try{
            Path parent = path.getParent();
            if(parent != null) Files.createDirectories(parent);
            List<QuoteRecord> quoteRecords = quotes.stream().map(q -> new QuoteRecord(q.getInstrument().getType(), q.getInstrument().getTicker(), q.getInstrument().getName(),
            q.getInstrument().getCurrency(), q.getPrice(), q.getChangePercent())).toList();
            mapper.writer().writeValue(path.toFile(), quoteRecords);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public List<Quote> load(Path path) {
        if(!Files.exists(path)){
            return List.of();
        }
        List<QuoteRecord> records;
        try {
             records =  mapper.readValue(path.toFile(), new TypeReference<>() {});
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return records.stream().map(q -> new Quote(InstrumentFactories.create(q.type(), q.ticker(), q.name(), q.currency()), q.price() , q.changePercent())).toList();
    }
}
