package alloy.gameobjects.collection;

import java.util.List;

import alloy.gameobjects.Case;

public class CaseCollection extends AbstractCollection<CaseCollection> {

    public CaseCollection(List<Case> cases) 
    {
        super(cases);
    }

    @Override
    public CaseCollection getData() 
    {
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Case> getList() 
    {
        return (List<Case>) super.getList();
    }

    
}
