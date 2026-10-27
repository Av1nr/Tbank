package academy.backend.market_pulse.history.impl;

import academy.backend.market_pulse.history.PriceHistoryStore;
import academy.backend.market_pulse.history.PriceHistoryStoreContractTest;


class PriceHistoryFileStoreTest extends PriceHistoryStoreContractTest {


    @Override
    protected PriceHistoryStore createStore() {
        return  new PriceHistoryFileStore();
    }
}