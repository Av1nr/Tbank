package academy.backend.market_pulse.repository.store.impl;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Заготовка для самостоятельной работы (см. «Требования к файловому хранилищу.md», семинар 7) —
 * структура есть, решения нет. Формат файла — на ваш выбор (JSON, построчный текст и т.д.); главное,
 * чтобы {@code load(save(tickers))} возвращал тот же набор тикеров в том же порядке. Проверьте себя
 * тестом {@code WatchlistStoreContractTest} (замените в нём эталонную реализацию на свою).
 */
public class WatchlistFileStore implements academy.backend.market_pulse.repository.store.WatchlistStore {
    @Override
    public void save(Path path, Set<String> tickers) {
        try {
            Path parent = path.getParent();
            if(parent != null) Files.createDirectories(parent);
            Files.write(path, tickers);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    @Override
    public Set<String> load(Path path) {
        if(!Files.exists(path)){
            return new LinkedHashSet<>();
        }
        Set<String> watchlist = new LinkedHashSet<>();
        List<String> lines;
        try {
            lines= Files.readAllLines(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for(String line : lines){
            if(line.isBlank()){
                continue;
            }
            watchlist.add(line.strip());
        }
        return watchlist;
    }
}
