package com.example.astrodream

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_avatar.*

class AvatarActivity : ActivityWithTopBar(R.string.avatar, R.id.dlAvatar) {

    lateinit var listAvatar: List<Int>
    lateinit var adapter: AvatarAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_avatar)

        listAvatar = listOf(
            R.drawable.ic_avatar_alien,
            R.drawable.ic_avatar_astronaut,
            R.drawable.ic_avatar_naked,
            R.drawable.ic_avatar_normal1,
            R.drawable.ic_avatar_normal2,
            R.drawable.ic_avatar_normal3,
            R.drawable.ic_avatar_normal4,
            R.drawable.ic_avatar_normal5,
            R.drawable.ic_avatar_nuts,
            R.drawable.ic_avatar_suit
        )

        adapter = AvatarAdapter(this, listAvatar)

        rvAvatar.adapter = adapter

        setUpMenuBehavior()
    }
}