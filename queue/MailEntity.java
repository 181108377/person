package queue;

import java.io.Serializable;

public class MailEntity implements Serializable, Comparable<MailEntity> {
	private String	name;
	private String	desc;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public int compareTo(MailEntity o) {
		return (o.getName() + o.getDesc()).equals(getName() + getDesc()) ? 0 : 1;
	}

	@Override
	public String toString() {
		return "MailEntity{" + "name='" + name + '\'' + ", desc='" + desc + '\'' + '}';
	}
}
