<template>
    <nav class="flex items-center justify-between flex-wrap bg-teal-500 p-4">
        <div class="w-full block flex-grow lg:flex lg:items-center lg:w-auto">
            <div class="text-sm lg:flex-grow">
                <router-link :to="`/report`" class="block mt-4 lg:inline-block lg:mt-0 text-teal-200 hover:text-white mr-4">
                    Report list
                </router-link>
                <router-link :to="`/about`" class="block mt-4 lg:inline-block lg:mt-0 text-teal-200 hover:text-white mr-4">
                    About
                </router-link>
            </div>
        </div>
    </nav>
</template>

<script>
export default {
  name: 'Header'
}
</script>