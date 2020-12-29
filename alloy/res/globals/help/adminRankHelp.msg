`set-xp`
sets the xp for a user.
ex: `set-xp 1000 @frontsnapk1ck`

`xp-blacklist-add`
add a channel to be blacklisted from users gaining xp in
ex: `xp-blacklist-add #spam`

`xp-blacklist-rm`
removed a channel from the list of channels that are blacklisted
ex: `xp-blacklist-rm #spam`


`xp-cooldown`
change the amount of time between users gaining xp
ex: `xp-cooldown 4`

`rankup-add`
add a custom message to be announced when someone reaches a new level
can also be used to assign roles to members when they rank up
`{user.ping}` will mention the user
`{user}` will get the user's tag
`{level}` will get the user's level
`{role}` will say the name of the role they recived if they recived a role
ex: `!rankup-add 5 @TestRankUP  {user.ping} {user} {level} {role}`
ex: `!rankup-add 5 {user.ping} {user} {level}`

`rankup-rm`
removed a custom message from a given level
ex: `rankup-rm 5`

`rankup-view`
shows a list of all the custom rankup messages