package org.emoflon.ibex.neo.benchmark.exttype2doc.concsync;

public enum ConflictType {

	ATTR('A'), CONTR_MOVE('C'), PRES_DEL('P'), MULTIPL('M');

	private char token;

	ConflictType(char token) {
		this.token = token;
	}

	public char getToken() {
		return token;
	}

	public static ConflictType valueOf(char token) {
		for (ConflictType t : ConflictType.values()) {
			if (t.token == token)
				return t;
		}
		throw new IllegalArgumentException("There is no value with token '" + token + "'");
	}

	public static ConflictType[] valuesOf(CharSequence tokenSequence) {
		ConflictType[] result = new ConflictType[tokenSequence.length()];
		for (int i = 0; i < tokenSequence.length(); i++)
			result[i] = ConflictType.valueOf(tokenSequence.charAt(i));
		return result;
	}

	public static String toTokens(Iterable<ConflictType> conflictTypes) {
		StringBuilder b = new StringBuilder();
		for (ConflictType t : conflictTypes)
			b.append(t.getToken());
		return b.toString();
	}

}
