<template>
  <div>
    <nav class="flex items-center justify-between flex-wrap bg-blue-500 p-2">
      <div class="w-full block flex-grow lg:flex lg:items-center lg:w-auto">
        <div class="text-sm lg:flex-grow">
          <div
            class="block mt-4 lg:inline-block lg:mt-0 text-teal-200 hover:text-white mr-4"
          >{{report.name}}</div>
          <div class="block mt-4 lg:inline-block lg:mt-0 text-teal-200 hover:text-white mr-4">|</div>
          <div
            class="block mt-4 lg:inline-block lg:mt-0 text-teal-200 hover:text-white mr-4"
          >{{report.description}}</div>
        </div>
      </div>
    </nav>
    <div class="flex">
      <!-- params -->
      <div class="w-1/3 max-w-xs">
        <form action="/data" method="get" class="bg-white px-8 pt-6 pb-8 mb-4">
          <div
            v-for="param in report.params"
            v-bind:key="param.name"
            class="md:flex md:items-center mb-6"
          >
            <div class="md:w-1/3">
              <label class="block text-gray-700 font-bold mb-2" :for="param.name">{{param.name}}</label>
            </div>
            <div class="md:w-2/3">
              <input
                v-model="params[param.name]"
                class="bg-gray-200 appearance-none border-2 border-gray-200 rounded w-full py-2 px-4 text-gray-700 leading-tight focus:outline-none focus:bg-white focus:border-teal-500"
                id="inline-full-name"
                type="text"
                :placeholder="param.name"
              >
            </div>
          </div>
          <div class="flex items-center justify-between">
            <button
              v-on:click="getResults"
              class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
              type="button"
            >Result</button>
            <button
              v-on:click="getCsv"
              class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
              type="button"
            >Csv</button>
          </div>
        </form>
      </div>
      <div class="bg-white my-6 w-2/3">
        <table class="text-left w-full border-collapse" style="border-collapse:collapse">
          <thead>
            <tr>
              <th
                v-for="header in results.header"
                v-bind:key="header"
                class="py-2 px-2 bg-grey-lighter font-sans font-regular text-sm text-grey border-b border-grey-light"
              >{{header}}</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="row in results.data" v-bind:key="row" class="hover:bg-blue-lightest">
              <td
                v-for="value in row"
                v-bind:key="value"
                class="py-1 px-1 border-b font-sans text-sm border-grey-light text-left"
              >{{value}}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
    <!-- results -->
  </div>
</template>

<script>
import axios from "axios";
import { log } from "util";

export default {
  name: "Report",
  data() {
    return {
      report: null,
      loading: true,
      errored: false,
      results: { header: [], data: [[]] },
      params: {}
    };
  },
  mounted() {
    axios
      .get("/report/" + this.$route.params.name)
      .then(response => {
        this.report = response.data;
      })
      .catch(error => {
        this.errored = true;
      })
      .finally(() => (this.loading = false));
  },
  methods: {
    getResults: function() {
      axios
        .post(
          this.$route.params.name + "/data",
          this.params
        )
        .then(r => {
          this.results = r.data;
        })
        .catch(e => {
          log(e);
        });
    },
    getCsv: function() {
      axios({
        url:
          this.$route.params.name + "/csv",
        method: "POST",
        responseType: "blob",
        data: this.params
      })
        .then(r => {
          const url = window.URL.createObjectURL(new Blob([r.data]));
          const link = document.createElement("a");
          link.href = url;
          link.setAttribute("download", "file.csv");
          document.body.appendChild(link);
          link.click();
        })
        .catch(e => {
          log(e);
        });
    },
    printData: function() {
      console.warn("Data:");
      console.warn(JSON.stringify(this.params));
    }
  }
};
</script>
