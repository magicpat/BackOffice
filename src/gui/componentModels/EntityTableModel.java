package gui.componentModels;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import bl.BL;
import dal.DALException;
import dal.DBEntity;
import dal.DBEntityInfo;
import dal.WhereChain;

public class EntityTableModel extends AbstractTableModel {
	private ArrayList<DBEntity> entries;
	private String filter = "";
	private String[] columnNames;
	public String[] columnNamesOriginal;
	private Class<?>[] columnTypes;
	private Class<? extends DBEntity> classT;

	private int parameter;

	private WhereChain where;

	public EntityTableModel(Class<? extends DBEntity> classT) {
		// this.classT = (Class<T>) ((ParameterizedType) getClass()
		// .getGenericSuperclass()).getActualTypeArguments()[0];

		this.classT = classT;
		entries = new ArrayList<DBEntity>();

		List<Field> fields = null;
		try {
			fields = DBEntityInfo.get(this.classT).getMergedFields();

		} catch (DALException e) {
			e.printStackTrace();
		}
		columnNames = new String[fields.size()];
		columnNamesOriginal = new String[fields.size()];
		columnTypes = new Class<?>[fields.size()];

		for (int i = 0; i < fields.size(); i++) {
			String name = fields.get(i).getName();
			columnNamesOriginal[i] = name;
			String format = Character.toUpperCase(name.charAt(0))
					+ name.substring(1);
			columnNames[i] = format;
			columnTypes[i] = fields.get(i).getType();
		}
		this.parameter = -1;
		this.refresh();
	}

	public EntityTableModel(Class<? extends DBEntity> classT, int parameter) {
		this(classT);
		this.parameter = parameter;
		this.refresh();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return entries.size();
	}

	public Object getValueAt(int row) {
		return entries.get(row);
	}

	@Override
	public Object getValueAt(int row, int col) {
		DBEntity entry = entries.get(row);
		try {
			Method method = classT.getMethod("get" + columnNames[col],
					new Class<?>[0]);
			Object a = method.invoke(entry, new Object[0]);
			return a;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	@Override
	public Class<?> getColumnClass(int column) {
		return columnTypes[column];
	}

	public String[] getColumnNames() {
		return columnNames;
	}

	public void setWhereChain(WhereChain where) {
		this.where = where;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public String getFilter() {
		return filter;
	}

	@SuppressWarnings("unchecked")
	public void refresh() {
		String property = classT.getName();
		property = property.substring(property.lastIndexOf('.') + 1);
		String getter = "get" + property + "Liste";

		try {
			if (where == null) {
				if (parameter == -1) {
					Method method = BL.class.getMethod(getter, new Class<?>[0]);
					entries = (ArrayList<DBEntity>) method.invoke(BL.class,
							new Object[0]);
				} else {
					Method method = BL.class.getMethod(getter, int.class);
					entries = (ArrayList<DBEntity>) method.invoke(BL.class,
							parameter);
				}
			} else {
				Method method = BL.class.getMethod(getter, WhereChain.class);
				entries = (ArrayList<DBEntity>) method.invoke(BL.class, where);
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		super.fireTableDataChanged();
	}

	public ArrayList<DBEntity> getEntries() {
		return entries;
	}

	public void setEntries(ArrayList<DBEntity> entries) {
		this.entries = entries;
	}

}
