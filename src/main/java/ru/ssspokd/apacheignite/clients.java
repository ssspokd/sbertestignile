package ru.ssspokd.apacheignite;

import org.apache.ignite.Ignition;
import org.apache.ignite.cache.query.FieldsQueryCursor;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.client.ClientCache;
import org.apache.ignite.client.ClientException;
import org.apache.ignite.client.IgniteClient;
import org.apache.ignite.configuration.ClientConfiguration;
import ru.ssspokd.apacheignite.model.EnumOperation;
import ru.ssspokd.apacheignite.model.Payment;

import java.util.Date;
import java.util.List;

public class clients {

    public static void main(String[] args) {
        //startClient
        ClientConfiguration cfg = new ClientConfiguration().setAddresses("127.0.0.1:11211");
        try (IgniteClient igniteClient = Ignition.startClient(cfg)) {
            System.out.println();
            System.out.println(">>> Thin client put-get example started.");
            Payment payment =  new Payment();
            payment.setId(1L);
            payment.setAccountUser("Payment");
            payment.setBalanse(5000L);
            payment.setLastOperationDate(new Date());
            payment.setEnumOperation(EnumOperation.NO_OPERATION);
            ClientCache clientCache = igniteClient.getOrCreateCache("PaymentA");
            clientCache.put(payment.getId(),payment);
            FieldsQueryCursor<List<? extends Payment>> cursor = clientCache.query(new SqlFieldsQuery(
                    "select * from Payment"));
            ClientCache clientCache1 = igniteClient.getOrCreateCache("PaymentB");
            QueryCursor<List<? extends Payment>> cursor1 = clientCache1.query(new SqlFieldsQuery(
                    "select * from Payment"));
            System.out.println(cursor.getAll());
            System.out.println(cursor1.getAll());

        } catch (ClientException e) {
            System.err.println(e.getMessage());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }

}
