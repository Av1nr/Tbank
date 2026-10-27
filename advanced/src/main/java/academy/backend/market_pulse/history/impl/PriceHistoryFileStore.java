package academy.backend.market_pulse.history.impl;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import academy.backend.market_pulse.history.PriceHistoryStore;
import academy.backend.market_pulse.history.PricePoint;

/**
 * Заготовка для самостоятельной работы (см. «Требования к файловому хранилищу.md», семинар 7) —
 * структура есть, решения нет. Формат файла — на ваш выбор (бинарный, текстовый и т.д.); главное,
 * чтобы каждый {@link #append} создавал НОВЫЙ файл (не переписывал предыдущий) и
 * {@link #load(Path, String)} возвращал все сохранённые точки по тикеру в порядке возрастания
 * времени. Проверьте себя тестом {@code PriceHistoryStoreContractTest} (замените в нём эталонную
 * реализацию на свою).
 */
public class PriceHistoryFileStore implements PriceHistoryStore {

    @Override
    public Path append(Path directory, PricePoint point) {
        try {
            Path tickerDir = Files.createDirectories(directory.resolve(point.ticker()));
            Path file = tickerDir.resolve(point.timestamp().toEpochMilli() + ".txt");
            return Files.writeString(file, point.price().toPlainString(), StandardCharsets.UTF_8, StandardOpenOption.CREATE_NEW);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public List<PricePoint> load(Path directory, String ticker) {
        Path tickerDir = directory.resolve(ticker);
        if (!Files.isDirectory(tickerDir)) {
            return List.of();
        }
        try(Stream<Path> files = Files.list(tickerDir)) {
            return files.map(file -> readPoint(ticker, file))
                    .sorted(Comparator.comparing(PricePoint::timestamp)).toList();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    private PricePoint readPoint(String ticker, Path path){
        try {
            return new PricePoint(ticker, Instant.ofEpochMilli(Long.parseLong(path.getFileName().toString().replace(".txt", ""))), new BigDecimal(Files.readString(path).strip()));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
