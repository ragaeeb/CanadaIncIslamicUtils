package com.canadainc.sunnah10;

public class RelatedNarration
{
	public int narrationId;
	public int otherId;

	public RelatedNarration(int narrationId, int otherId) {
		super();
		this.narrationId = narrationId;
		this.otherId = otherId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result;
		result = prime * result + narrationId + otherId;
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
		RelatedNarration other = (RelatedNarration) obj;
		return (narrationId == other.narrationId && narrationId == other.otherId) || (narrationId == other.otherId && otherId == other.narrationId);
	}
}