package academy.backend.market_pulse.repository.store.impl;

import academy.backend.market_pulse.repository.store.WatchlistFileStoreContractTest;
import academy.backend.market_pulse.repository.store.WatchlistStore;

class WatchlistFileStoreTest extends WatchlistFileStoreContractTest {

    @Override
    protected WatchlistStore createStore() {
        return new WatchlistFileStore();
    }
}