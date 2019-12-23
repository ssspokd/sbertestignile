package ru.ssspokd.apacheignite.store;

import org.apache.ignite.cache.store.CacheStoreAdapter;
import org.apache.ignite.cache.store.CacheStoreSession;
import org.apache.ignite.lang.IgniteBiInClosure;
import org.apache.ignite.resources.CacheStoreSessionResource;
import ru.ssspokd.apacheignite.model.EnumOperation;
import ru.ssspokd.apacheignite.model.Payment;

import javax.cache.Cache;
import javax.cache.integration.CacheLoaderException;
import javax.cache.integration.CacheWriterException;
import java.sql.*;

public class CacheStore extends CacheStoreAdapter<Long, Payment> {

    @CacheStoreSessionResource
    private CacheStoreSession ses;

    @Override
    public Payment load(Long key) throws CacheLoaderException {
        try (Connection conn = connection()) {
            try (PreparedStatement st = conn.prepareStatement("select * from Payment where id=?")) {
                st.setLong(1, key);
                ResultSet rs = st.executeQuery();
                return rs.next() ? new Payment(rs.getLong(1),
                        rs.getString(2),
                        rs.getLong(3),
                        rs.getDate(4),
                        EnumOperation.valueOf(rs.getString(5))) : null;
            }
        }
        catch (SQLException e) {
            throw new CacheLoaderException("Failed to load: " + key, e);
        }
    }

    @Override
    public void write(Cache.Entry<? extends Long, ? extends Payment> entrys) throws CacheWriterException {
        try (Connection conn = connection()) {
            System.out.println("not yet no");
           /* // Syntax of MERGE statement is database specific and should be adopted for your database.
            // If your database does not support MERGE statement then use sequentially update, insert statements.
            try (PreparedStatement st = conn.prepareStatement("" +
                    "")
            {
                for (Cache.Entry<? extends Long, ? extends Payment> entry : entrys) {
                    Payment val = entry.getValue();
                    st.setLong(1, entry.getKey());
                    st.setString(2, val.getFirstName());
                    st.setString(3, val.getLastName());

                    st.executeUpdate();
                }
            }*/
        }
        catch (SQLException e) {
            //throw new CacheWriterException("Failed to write [key=" + key + ", val=" + val + ']', e);
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Object key) throws CacheWriterException {
        try (Connection conn = connection()) {
            try (PreparedStatement st = conn.prepareStatement("delete from Payment where id=?")) {
                st.setLong(1, (Long)key);

                st.executeUpdate();
            }
        }
        catch (SQLException e) {
            throw new CacheWriterException("Failed to delete: " + key, e);
        }
    }

    // This mehtod is called whenever "loadCache()" and "localLoadCache()"
    // methods are called on IgniteCache. It is used for bulk-loading the cache.
    // If you don't need to bulk-load the cache, skip this method.
    @Override public void loadCache(IgniteBiInClosure<Long, Payment> clo, Object... args) {
        if (args == null || args.length == 0 || args[0] == null)
            throw new CacheLoaderException("Expected entry count parameter is not provided.");
        final int entryCnt = (Integer)args[0];

        try (Connection conn = connection()) {
            try (PreparedStatement st = conn.prepareStatement("select * from Payment")) {
                try (ResultSet rs = st.executeQuery()) {
                    int cnt = 0;

                    while (cnt < entryCnt && rs.next()) {
                        Payment payment = new Payment(rs.getLong(1),
                                rs.getString(2),
                                rs.getLong(3),
                                rs.getDate(4),
                                EnumOperation.valueOf(rs.getString(5)));
                        clo.apply(payment.getId(),payment);
                        cnt++;
                    }
                }
            }
        }
        catch (SQLException e) {
            throw new CacheLoaderException("Failed to load values from cache store.", e);
        }
    }

    // Open JDBC connection.
    private Connection connection() throws SQLException  {
        // Open connection to your RDBMS systems (Oracle, MySQL, Postgres, DB2, Microsoft SQL, etc.)
        // In this example we use H2 Database for simplification.
        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/test");
        conn.setAutoCommit(true);
        return conn;
    }
}
