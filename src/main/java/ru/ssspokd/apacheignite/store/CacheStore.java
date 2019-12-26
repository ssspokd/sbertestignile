package ru.ssspokd.apacheignite.store;

import org.apache.ignite.cache.store.CacheStoreAdapter;
import org.apache.ignite.lang.IgniteBiInClosure;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import ru.ssspokd.apacheignite.config.DataSourcesConfig;
import ru.ssspokd.apacheignite.model.EnumOperation;
import ru.ssspokd.apacheignite.model.Payment;

import javax.cache.Cache;
import javax.cache.integration.CacheLoaderException;
import javax.cache.integration.CacheWriterException;
import java.util.Map;


@Service
public class CacheStore extends CacheStoreAdapter<Long, Payment> {


    /** Spring JDBC template. */
    private JdbcTemplate jdbcTemplate;


    public CacheStore() {
        this.jdbcTemplate = new JdbcTemplate( new DataSourcesConfig().dataSource());
    }


    @SuppressWarnings("unchecked")
    public Payment loadLastOperation(String accountuser){
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select id, accountuser, \n" +
                "accountbalance, lastoperationdate, enumoperation FROM Payment WHERE " +
                        " accountuser = '"+accountuser
                +"' ORDER BY lastoperationdate DESC LIMIT 1");
        Payment payment = null;
        if(rowSet.next()) {
            payment =  new Payment();
            payment.setId(rowSet.getLong(1));
            payment.setAccountUser(rowSet.getString(2));
            payment.setBalanse(rowSet.getLong(3));
            payment.setLastOperationDate(rowSet.getDate(4));
            payment.setEnumOperation(EnumOperation.valueOf(rowSet.getString(5)));
        }
        return  payment;
    }

    @Override
    public Payment load(Long key) throws CacheLoaderException {
        return new Payment();
    }

    @Override
    public void write(Cache.Entry<? extends Long, ? extends Payment> entry) throws CacheWriterException {
        Long key = entry.getKey();
        Payment val = entry.getValue();
        jdbcTemplate.update("insert into Payment " +
                        "(id, accountuser, accountbalance, lastoperationdate, enumoperation)" +
                        " values (?, ?, ?,?,?)",
                key,
                val.getAccountUser(),
                val.getBalanse(),
                val.getLastOperationDate(),
                val.getEnumOperation().name());
    }

    @Override
    public void delete(Object o) throws CacheWriterException {
        new Throwable("not yet");
    }

    @Override
    public void loadCache(IgniteBiInClosure<Long, Payment> clo, Object... args) {
        super.loadCache(clo, args);
    }

    @Override
    public Map<Long, Payment> loadAll(Iterable<? extends Long> keys) {
        return super.loadAll(keys);
    }


}
