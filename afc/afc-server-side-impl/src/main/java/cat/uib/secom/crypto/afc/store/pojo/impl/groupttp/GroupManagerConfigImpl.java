package cat.uib.secom.crypto.afc.store.pojo.impl.groupttp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import cat.uib.secom.crypto.afc.store.pojo.groupttp.GroupManagerConfig;


@Entity
@Table(name="GROUP_TTP_CONFIG")
@NamedQuery(name="getProperty",
			query="SELECT p FROM GroupManagerConfigImpl p WHERE p.property = :property")
public class GroupManagerConfigImpl implements GroupManagerConfig {

	@Id
	@GeneratedValue
	@Column(name="ID") 
	private Long id;
	
	@Column(name="PROPERTY_NAME")
	private String property;
	
	@Column(name="PROPERTY_VALUE")
	private byte[] value;

	
	
	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	
	
	public void setProperty(String property) {
		this.property = property;
	}

	public String getProperty() {
		return property;
	}

	
	
	public void setValue(byte[] value) {
		this.value = value;
	}

	public byte[] getValue() {
		return value;
	}
	
	
}
