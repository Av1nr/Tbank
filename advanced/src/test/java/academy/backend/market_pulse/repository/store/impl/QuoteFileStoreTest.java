package academy.backend.market_pulse.repository.store.impl;

import academy.backend.market_pulse.repository.store.QuoteFileStoreContractTest;

class QuoteFileStoreTest extends QuoteFileStoreContractTest {

    @Override
    protected QuoteFileStore createStore() {
        return new QuoteFileStore();
    }
}