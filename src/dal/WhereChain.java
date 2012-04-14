package dal;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Patrick Stapfer
 * Date: 13.04.12
 * Time: 16:52
 */
public class WhereChain {
    private List<WhereCondition> conditions = new ArrayList<WhereCondition>();

    /**
     * Initializes the new WhereChaine like in the give init(...) method
     * @param fieldname - Fieldname for the where condition
     * @param operator - WhereOperator for comparison
     * @param value - Value for the field-condition
     */
    public WhereChain(String fieldname, WhereOperator operator, Object value){
        init(fieldname, operator, value);
    }


    /**
     * Initializes the new WhereChain like in the given init(...) ethod
     * @param entityClass - Class, which fields should be retrieved for the WHERE-Conditions
     * @param operator  - Operator which should be used for each condition
     * @param value - Value which should be used for each condition
     * @param chainword - Chainword, which links the WHERE-Clausels together (for instance OR / AND,...)
     */
    public WhereChain(Class<? extends DBEntity> entityClass, WhereOperator operator, Object value, Chainer chainword){
        init(entityClass, operator, value, chainword);
    }

    /**
     * This method REINITIALIZES the WhereChain and sets the initial condition, the base of the whole chain on
     * which each other condition builds on
     * Warning: Existing where-conditions will be dropped!
     * @param fieldname - Fieldname for the where condition
     * @param operator - WhereOperator for comparison
     * @param value - Value for the field-condition
     */
    public void init(String fieldname, WhereOperator operator, Object value){
        if(!conditions.isEmpty())
            clear();

        addCondition(null, fieldname, operator, value);
    }

    /**
     * This method REINITIALIZES the whereChain, that means, existing where-conditions will be dropped!
     * It constructs a WhereCondition-Chain for each field in the given class-definiton with the same Operator and
     * value and links them with the given chainword.
     * For instance:
     * WhereCondition(Angebot.class, WhereOperator.LIKE, "%max%", WhereChain.Chainer.OR)
     * Creates following syntactic WHERE-Clausel:
     * "WHERE angebotID LIKE '%max%' AND datum LIKE '%max%' AND...."
     * @param entityClass - Class, which fields should be retrieved for the WHERE-Conditions
     * @param operator  - Operator which should be used for each condition
     * @param value - Value which should be used for each condition
     * @param chainword - Chainword, which links the WHERE-Clausels together (for instance OR / AND,...)
     */
    public void init(Class<? extends DBEntity> entityClass, WhereOperator operator, Object value, Chainer chainword){
        for(Field f : DBEntity.getAllDeclaredFields(entityClass)){
            addCondition(chainword, f.getName(), operator, value);
        }
    }

    /**
     * Adds a AND-Condition to the other conditions
     * It will be intepreted like that:
     * WHERE [condition] AND [your new condition] ...
     * @param fieldname - Fieldname for the where condition
     * @param operator - WhereOperator for comparison
     * @param value - Value for the field-condition
     */
    public void addAndCondition(String fieldname, WhereOperator operator, Object value){
        addCondition(Chainer.AND, fieldname, operator, value);
    }

    /**
     * Adds a OR-Condition to the other conditions
     * It will be intepreted like that:
     * WHERE [condition] OR [your new condition] ...
     * @param fieldname - Fieldname for the where condition
     * @param operator - WhereOperator for comparison
     * @param value - Value for the field-condition
     */
    public void addOrCondition(String fieldname, WhereOperator operator, Object value){
        addCondition(Chainer.OR, fieldname, operator, value);
    }

    /**
     * Utilitymethod just for creating a new WhereCondition
     * Only for internal usage.
     * @param chainer - Chainword, which links the WHERE-Clausels together (for instance OR / AND,...)
     * @param fieldname - Fieldname for the where condition
     * @param operator - WhereOperator for comparison
     * @param value - Value for the field-condition
     */
    private void addCondition(Chainer chainer, String fieldname, WhereOperator operator, Object value){
        conditions.add(new WhereCondition(chainer, fieldname, operator, value));
    }

    /**
     * Returns all saved conditions.
     * Since this WhereChain should hide the complex system of creating a WHERE-Clausel, this method is protected and
     * only for the DAL-package useable.
     * Made for the DatabaseAdapters.
     * @return List of all WhereConditions chained together
     */
    protected List<WhereCondition> getConditions(){
        return conditions;
    }

    /**
     * Clears the list of conditions
     */
    public void clear(){
        conditions.clear();
    }

    /**
     * Enum for the linkwords
     */
    public static enum Chainer {
        AND{
            public String toString(){
                return "AND";
            }
        },
        OR{
            public String toString(){
                return "OR";
            }
        },
    }

}
