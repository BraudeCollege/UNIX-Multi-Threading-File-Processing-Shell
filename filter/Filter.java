package filter;

public abstract class Filter {

	public static final String FILE_SEPARATOR = System.getProperty("file.separator");
	protected Filter next = null;
	protected Filter prev = null;

	public abstract void setNextFilter(Filter next);

	public abstract void setPrevFilter(Filter next);

	public abstract boolean isDone();

}
