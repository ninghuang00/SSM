package cn.edu.art.ssm.model;

public class Test {
    private String testid;

    private String testname;

    public Test(String testid, String testname) {
        this.testid = testid;
        this.testname = testname;
    }

    public Test() {
        super();
    }

    public String getTestid() {
        return testid;
    }

    public void setTestid(String testid) {
        this.testid = testid == null ? null : testid.trim();
    }

    public String getTestname() {
        return testname;
    }

    public void setTestname(String testname) {
        this.testname = testname == null ? null : testname.trim();
    }
}