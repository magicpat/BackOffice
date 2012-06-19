package dal;

import bl.objects.Angebot;
import bl.objects.Eingangsrechnung;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Patrick Stapfer
 * Date: 13.04.12
 * Time: 11:19
 */
public class MysqlAdapterTest {
    DatabaseAdapter db = null;

    @Before
    public void setUp() throws Exception {
        db = new MysqlAdapter("root","4chaos", "localhost", "backoffice");
    }

    @Test
    public void testAddEntity() throws Exception {

        db.connect();

        Angebot a = new Angebot();
        a.setChance(20.0);
        a.setDatum(GregorianCalendar.getInstance().getTime());
        a.setDauer(100.0);
        a.setSumme(20.0);

        Object newKey = db.addEntity(a);

        Assert.assertNotNull("Insert did not succeed", newKey);

        db.connect();

        db.disconnect();

    }

    @Test
    public void testGetEntityByID() throws Exception {
        db.connect();
        Angebot a = db.getEntityByID(1, Angebot.class);
        System.out.println(a);
        db.disconnect();
    }

    @Test
    public void testUpdateEntity() throws Exception {

    }

    @Test
    public void testDeleteEntity() throws Exception {

    }

    @Test
    public void testGetEntityList() throws Exception {
        System.out.println("\n\nALL ENTITIES");
        db.connect();

        List<Angebot> angebote = db.getEntityList(Angebot.class);
        for(Angebot a : angebote){
            System.out.println(a);
        }
        db.disconnect();
    }

    @Test
    public void testGetEntitiesBy() throws Exception {
        System.out.println("\n\nENTITIES BY WHERE CLAUSEL");
        db.connect();
        WhereChain where = new WhereChain("angebotID", WhereOperator.GREATEREQUALS, 1);
        where.addAndCondition("chance", WhereOperator.GREATER, 10.0);

        List<Angebot> angebote = db.getEntitiesBy(where, Angebot.class);
        for(Angebot a : angebote){
            System.out.println(a);
        }
        db.disconnect();
    }


    @Test
    public void testAllGetMethods() throws Exception{
        db.connect();
        List<Angebot> liste = db.getEntityList(Angebot.class);

        for(Angebot a : liste){
            System.out.println(a);
        }

        List<Eingangsrechnung> liste2 = db.getEntityList(Eingangsrechnung.class);

        for(Eingangsrechnung a : liste2){
            System.out.println(a);
        }

        System.out.println("\n\n GETENTITYBYID:");
        Eingangsrechnung e = db.getEntityByID(1, Eingangsrechnung.class);
        System.out.println(e);

        System.out.println("\n\n GETENTITIESBY WHERE:");
        WhereChain where = new WhereChain("rechnungID", WhereOperator.GREATEREQUALS, 1);
        where.addAndCondition("status", WhereOperator.LIKE, "off");

        List<Eingangsrechnung> liste3 = db.getEntitiesBy(where, Eingangsrechnung.class);
        for(Eingangsrechnung a : liste3){
            System.out.println(a);
        }

        System.out.println("\n\n FILTER WHERCHAIN:");
        where = new WhereChain(Eingangsrechnung.class, WhereOperator.EQUALS, "offen", WhereChain.Chainer.OR);

        List<Eingangsrechnung> liste4 = db.getEntitiesBy(null, Eingangsrechnung.class);
        for(Eingangsrechnung a : liste4){
            System.out.println(a);
        }

        db.disconnect();
    }

}
