package gui.componentModels;

import java.awt.Component;
import java.lang.reflect.Method;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

public class MyListCellRenderer extends DefaultListCellRenderer {
	private String getter, getter2;

	public MyListCellRenderer(String property) {
		super();
		getter = "getID";
		getter2 = "get" + Character.toUpperCase(property.charAt(0))
				+ property.substring(1);
	}

	public MyListCellRenderer() {
		super();
		getter = "getID";
	}

	private Object getPropertyValue(Object object) {
		try {
			Method method = object.getClass()
					.getMethod(getter, new Class<?>[0]);
			if (getter2 != null) {
				Method method2 = object.getClass().getMethod(getter2,
						new Class<?>[0]);

				return method.invoke(object, new Object[0]) + " - "
						+ method2.invoke(object, new Object[0]);
			} else {
				return method.invoke(object, new Object[0]);
			}
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return null;
	}

	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		Object property = getPropertyValue(value);
		return super.getListCellRendererComponent(list, property, index,
				isSelected, cellHasFocus);
	}
}