package frontsnapk1ck.alloy.gameobjects.collection;

import java.util.List;

import frontsnapk1ck.alloy.gameobjects.Case;
import frontsnapk1ck.utility.cache.AbstractCollection;

public class CaseCollection extends AbstractCollection<Case> {

    public CaseCollection(List<Case> cases) 
    {
        super(cases);
    }

    @Override
    public CaseCollection getData() 
    {
        return this;
    }
    
}
