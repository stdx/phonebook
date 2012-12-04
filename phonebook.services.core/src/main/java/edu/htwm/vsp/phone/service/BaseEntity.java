package edu.htwm.vsp.phone.service;

import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@MappedSuperclass
public abstract class BaseEntity {
	

	
	@Version
	private int version;


	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + version;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BaseEntity other = (BaseEntity) obj;
		if (version != other.version)
			return false;
		return true;
	}
	
}
