package appmanaged;

import cz.nguyen.entity.Company;
import cz.nguyen.entity.Employee;

class CompanyEmployeePair {
    private Company c;
    private Employee e;
    
    public CompanyEmployeePair(Company c, Employee e) {
        this.c = c;
        this.e = e;
    }

    @Override
    public String toString() {
        return "CompanyEmployeePair [c=" + c.getName() + ", e=" + e.getName() + "]";
    }
}