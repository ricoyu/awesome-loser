package com.loserico.orm.predicate;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDatePredicate extends AbstractPredicate{
	
	private List<DateMatchMode> candidateMatchModes = new ArrayList<>();
	private DateMatchMode matchMode = DateMatchMode.EARLIER_THAN;
	
	public AbstractDatePredicate() {
	}
	
	public AbstractDatePredicate(DateMatchMode matchMode) {
		this.setMatchMode(matchMode);
	}

	protected void addCandidateMatchMode(DateMatchMode... matchModes) {
		for (DateMatchMode matchMode : matchModes) {
			candidateMatchModes.add(matchMode);
		}
	}

	/**
	 * 检查matchMode是否合法
	 * 
	 * @return
	 */
	protected void checkDateMatchMode(DateMatchMode matchMode) {
		boolean isLegal = false;
		for (DateMatchMode candidateMatchMode : candidateMatchModes) {
			if (candidateMatchMode.equals(matchMode)) {
				isLegal = true;
				break;
			}
		}

		if(! isLegal) {
			throw new IllegalArgumentException("Can only accept DateMatchMode: " + toCandidateString());
		}
	}

	private String toCandidateString() {
		StringBuilder sBuilder = new StringBuilder();
		for (DateMatchMode dateMatchMode : candidateMatchModes) {
			sBuilder.append(dateMatchMode.toString()).append(",");
		}
		sBuilder.deleteCharAt(sBuilder.length() - 1);

		return sBuilder.toString();
	}

	public DateMatchMode getMatchMode() {
		return matchMode;
	}

	public void setMatchMode(DateMatchMode matchMode) {
		this.matchMode = matchMode;
	}

}
