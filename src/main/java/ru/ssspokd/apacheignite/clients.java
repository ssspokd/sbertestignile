package ru.ssspokd.apacheignite;

import org.apache.ignite.Ignition;
import org.apache.ignite.cache.query.FieldsQueryCursor;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.client.ClientCache;
import org.apache.ignite.client.ClientException;
import org.apache.ignite.client.IgniteClient;
import org.apache.ignite.configuration.ClientConfiguration;
import org.apache.ignite.resources.SpringResource;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import ru.ssspokd.apacheignite.model.EnumOperation;
import ru.ssspokd.apacheignite.model.Payment;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class clients {

    @SpringResource(resourceClass = ru.ssspokd.apacheignite.config.DataSourcesConfig.class)
    DataSource dataSource;

    private  static  Logger LOGGER = Logger.getLogger(clients.class);
    static {
        LOGGER.setLevel(Level.ALL);
        LOGGER.setAdditivity(true);
        org.apache.log4j.BasicConfigurator.configure();
    }

    public static void main(String[] args) {
        clients clients = new clients();
        DataSource dataSource = clients.dataSource;
        ClientConfiguration cfg = new ClientConfiguration().setAddresses("127.0.0.1:11211");
        try (IgniteClient igniteClient = Ignition.startClient(cfg)) {
            LOGGER.info("------------------------------------------------");
            LOGGER.info("------------------------------------------------");
            LOGGER.info("------------------------------------------------");
            printCach(igniteClient.getOrCreateCache("PaymentA"));
            printCach(igniteClient.getOrCreateCache("PaymentB"));
            LOGGER.info("------------------------------------------------");
            LOGGER.info("------------------------------------------------");
            LOGGER.info("------------------------------------------------");
            process(igniteClient.getOrCreateCache("PaymentA"),
                    igniteClient.getOrCreateCache("PaymentB"),
                    50);
            LOGGER.info("------------------------------------------------");
            LOGGER.info("------------------------------------------------");
            LOGGER.info("------------------------------------------------");
            printCach(igniteClient.getOrCreateCache("PaymentA"));
            printCach(igniteClient.getOrCreateCache("PaymentB"));

            printReport( igniteClient);
        } catch (ClientException e) {
            System.err.println(e.getMessage());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }

    private static void printReport(IgniteClient igniteClient){
        LOGGER.info("------------------------------------------------");
        LOGGER.info("------------------------------------------------");
        LOGGER.info("------------REPORT------------------------------");
        LOGGER.info("------------------------------------------------");
        LOGGER.info("------------------------------------------------");
        ClientCache cacheA = igniteClient.getOrCreateCache("PaymentA");
        ClientCache cacheB = igniteClient.getOrCreateCache("PaymentB");
        Object o = cacheA.query(new SqlFieldsQuery("select * from payment")).getAll().get(0);
        Object o1 = cacheB.query(new SqlFieldsQuery("select * from payment")).getAll().get(0);
        ArrayList A =  ((ArrayList) o);
        ArrayList B =  ((ArrayList) o1);
        LOGGER.info("amountBalance PaymentA: " + A.get(2));
        LOGGER.info("amountBalance PaymentB: " + B.get(2));
        long result = Long.valueOf(A.get(2).toString()) + Long.valueOf(B.get(2).toString());
        LOGGER.info("balance PaymnetA and PaymentB:" + result);

    }

    private static void process(ClientCache<Long, Payment> cacheA,
                                ClientCache<Long, Payment> cacheB,
                                int countRandomOperation) {
        for(int i = 0; i < countRandomOperation; i ++) {
            LOGGER.info("iteration number:" + i);
            Payment paymentA = cacheA.get(1L);
            Payment paymentB = cacheB.get(1L);
            long ammountPaymentA = paymentA.getBalanse();
            long ammountPaymentB = paymentB.getBalanse();
            long ammountForTransfer = ThreadLocalRandom.current().nextLong(1, 100);
            int randomInt = ThreadLocalRandom.current().nextInt(0,2);
            LOGGER.info("ammountBalanseA:" + paymentA.getBalanse() + "\n " +
                    "ammountBalanseA:" + paymentA.getBalanse());
            LOGGER.info("random int = " + randomInt);
            if (randomInt == 1) {
                paymentA.setBalanse(ammountPaymentA - ammountForTransfer);
                paymentB.setBalanse(ammountPaymentB + ammountForTransfer);
                paymentA.setLastOperationDate(new Date());
                paymentA.setEnumOperation(EnumOperation.AMOUNT_TRANSFER);
                paymentB.setEnumOperation(EnumOperation.CREDIT_AMOUNT);
                paymentB.setLastOperationDate(new Date());
            }
            else {
                paymentA.setBalanse(ammountPaymentA + ammountForTransfer);
                paymentB.setBalanse(ammountPaymentB - ammountForTransfer);
                paymentA.setLastOperationDate(new Date());
                paymentA.setEnumOperation(EnumOperation.CREDIT_AMOUNT);
                paymentB.setEnumOperation(EnumOperation.AMOUNT_TRANSFER);
                paymentB.setLastOperationDate(new Date());
            }
            cacheA.put(1L,paymentA);
            cacheB.put(1L,paymentB);
            printCach(cacheA);
            printCach(cacheB);
        }
    }

    private static void  printCach(ClientCache clientCache){
        FieldsQueryCursor<List<? extends Payment>> cursor = clientCache.query(new SqlFieldsQuery(
                "select * from payment"));
        LOGGER.info(cursor.getAll());
    }

}
